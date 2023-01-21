package uilibrary;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;

/**
 * DragAndDrop TransferHandler.
 * Use it by passing it to <code>JComponent's</code> <code>setTransferHandler</code> method.
 * <p>
 * Simple usage is:
 * <code>jFrameWindow.setTransferHandler(new DragAndDrop(this::openFile));</code>
 * <p>
 * Then it will call <code>this::openFile</code> method with the file list that was dropped with drag and drop.
 * <p>
 * Requires java 8.
 */
public class DragAndDrop extends TransferHandler {
	private Function<TransferSupport, Boolean> onCanImport;
	private Function<List<File>, Boolean> onImportFile;
	private Function<String, Boolean> onImportText;
	
	public DragAndDrop()  {}
	
	/**
	 * Creates a DragAndDrop TransferHandler.
	 * <p>
	 * For more details on how to create these functions, see
	 * {@link DragAndDrop#DragAndDrop(Function<TransferSupport, Boolean>, Function<List<File>, Boolean>, Function<String, Boolean>)}
	 * @param onImportFile 
	 */
	public DragAndDrop(Function<List<File>, Boolean> onImportFile)  {
		this(null, onImportFile);
	}
	
	public DragAndDrop(Function<TransferSupport, Boolean> onCanImport, Function<List<File>, Boolean> onImportFile)  {
		this(onCanImport, onImportFile, null);
	}
	
	/**
	 * Creates a DragAndDrop TransferHandler.
	 * <p>
	 * It takes three functions, <code>onCanImport</code> which is called when <code>canImport</code> is called,
	 * <code>onImportFile</code> which is called when <code>importData</code> is called here with a transfer object of files, and
	 * <code>onImportText</code> which is called when <code>importData</code> is called here with a transfer object of text.
	 * They return a <code>boolean</code>. For <code>onCanImport</code> whether or not the transfer is supported.
	 * For <code>onImportFile</code> and <code>onImportText</code> whether or not the transfer was successful.
	 * <p>
	 * Create a function with one of these methods:
	 * <ul>
	 * <li>Lambda expression:
	 * <ul>
	 * <li>One-liner: <code>input -> input.returnsAValue()</code></li>
	 * <p>
	 * <li>In block form:</li>
	 * </ul>
	 * <pre>
	 * {@code
	 *	Function<List<File>, Boolean> onImportFile = fileList -> {
	 *		this.openFile(fileList.get(0));
	 *		return true;
	 *	};
	 * }</pre>
	 * </li>
	 * 
	 * <li>Reference a method that has the same input and return value.
	 * 
	 * <ul>
	 *	<li>You can reference it statically with <code>ClassName::methodName</code></li>
	 *	<li>and non-statically with <code>this::methodName</code> or <code>objectVariable::methodName</code></li>
	 * </ul>
	 * <p>
	 * Like this:
	 * <code>new DragAndDrop(this::openFile);</code>
	 * </ul>
	 * @param onCanImport
	 * @param onImportFile 
	 * @param onImportText 
	 */
	public DragAndDrop(Function<TransferSupport, Boolean> onCanImport, Function<List<File>, Boolean> onImportFile, Function<String, Boolean> onImportText)  {
		this.onCanImport = onCanImport;
		this.onImportFile = onImportFile;
		this.onImportText = onImportText;
	}
	
	public void setOnCanImport(Function<TransferSupport, Boolean> onCanImport) {
		this.onCanImport = onCanImport;
	}
	
	public void setOnImportFile(Function<List<File>, Boolean> onImportFile) {
		this.onImportFile = onImportFile;
	}
	
	public void setOnImportText(Function<String, Boolean> onImportText) {
		this.onImportText = onImportText;
	}
	
	/**
	 * This will be called repeatedly during drag and drop.
	 * Set the properties you want on every call. Support will be a fresh state.
	 * This will be called right before importData as well, and importData will have the same support from the last call to canImport.
	 * 
	 * @param support
	 * @return <code>true</code> if the import can happen, <code>false</code> otherwise
	 */
	@Override
	public boolean canImport(TransferSupport support) {
		if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor) && !support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			return false;
		}
		
		
		/*if (shouldCopy()) {
			boolean copySupported = (COPY & support.getSourceDropActions()) == COPY;

			if (!copySupported) {
				return false;
			}

			support.setDropAction(COPY);
		}*/
		
		if (onCanImport == null) {
			return true;
		}
		
		return onCanImport.apply(support);
	}
	
	/**
	 * Will be called when dropping the drag and drop.
	 * <code>canImport</code> will be called right before, and the support will be the same object.
	 * <p>
	 * Calls onCanImport function with the list of files that was dropped.
	 * Does not call it with empty list.
	 * @param support
	 * @return Was the drop successful or not
	 */
	@Override
	public boolean importData(TransferSupport support) {
		Transferable transferable = support.getTransferable();
		
		try {
			DataFlavor[] flavors = transferable.getTransferDataFlavors();
			
			for (DataFlavor flavor : flavors) {
				if (onImportFile != null && flavor.isFlavorJavaFileListType()) {
					List<File> list = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
					if (list.isEmpty()) return false;
					
					return onImportFile.apply(list);
				} else if (onImportText != null && flavor.isFlavorTextType()) {
					String text = (String) transferable.getTransferData(DataFlavor.stringFlavor);
					if (text.isEmpty()) return false;
					
					return onImportText.apply(text);
				}
			}
		} catch (UnsupportedFlavorException | IOException e) {
			System.err.println("Format is not supported");
		}
		
		return false;
	}
}
