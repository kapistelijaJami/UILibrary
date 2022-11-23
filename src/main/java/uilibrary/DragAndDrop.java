package uilibrary;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import javax.swing.TransferHandler;

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
	private Function<List<File>, Boolean> onImportData;
	
	/**
	 * Creates a DragAndDrop TransferHandler.
	 * <p>
	 * For more details on how to create these functions, see
	 * {@link DragAndDrop#DragAndDrop(Function<TransferHandler.TransferSupport, Boolean>, Function<List<File>, Boolean>)}
	 * @param onImportData 
	 */
	public DragAndDrop(Function<List<File>, Boolean> onImportData)  {
		this(null, onImportData);
	}
	
	/**
	 * Creates a DragAndDrop TransferHandler.
	 * <p>
	 * It takes two functions, onCanImport which is called when canImport is called, and
	 * onImportData which is called when importData is called here.
	 * They return a <code>boolean</code> same way as their matching methods.
	 * <p>
	 * Create the function with either lambda expression:
	 * input -> input.returnsAValue()
	 * <p>
	 * or like this with a lambda expression:
	 * <pre>
	 * {@code
	 *	Function<List<File>, Boolean> onImportData = fileList -> {
	 *		this.openFile(fileList.get(0));
	 *		return true;
	 *	};
	 * }
	 * </pre>
	 * 
	 * OR just reference a method that has the same input and return value.
	 * <p>
	 * You can reference it statically with <code>ClassName::methodName</code>,
	 * and non-statically with <code>this::methodName</code> or <code>objectVariable::methodName</code>.
	 * <p>
	 * Like this:
	 * <code>new DragAndDrop(this::openFile);</code>
	 * 
	 * @param onCanImport
	 * @param onImportData 
	 */
	public DragAndDrop(Function<TransferSupport, Boolean> onCanImport, Function<List<File>, Boolean> onImportData)  {
		this.onCanImport = onCanImport;
		this.onImportData = onImportData;
	}
	
	/**
	 * This will be called repeatedly during drag and drop.
	 * Set the properties you want on every call. Support will be fresh state.
	 * This will be called right before importData as well, and importData will have the same support from the last call to canImport.
	 * 
	 * @param support
	 * @return <code>true</code> if the import can happen, <code>false</code> otherwise
	 */
	@Override
	public boolean canImport(TransferSupport support) {
		if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
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
	 * <code>canImport</code> will be called right before, and the support will be the same.
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
			List<File> list = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor); //TODO: allow importing of text
			if (list.isEmpty()) return false;
			
			return onImportData.apply(list);
			
		} catch (UnsupportedFlavorException | IOException e) {
			return false;
		}
	}
}
