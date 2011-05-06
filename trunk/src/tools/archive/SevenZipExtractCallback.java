/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.archive;

import java.io.FileOutputStream;
import java.io.IOException;
import net.sf.sevenzipjbinding.ExtractAskMode;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IArchiveExtractCallback;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.ISevenZipInArchive;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZipException;

/**
 *
 * @author ssoldatos
 */
public class SevenZipExtractCallback implements IArchiveExtractCallback {

  private final ISevenZipInArchive inArchive;
  private final String extractPath;
  private int type;
  private final SevenZipFile sevenZip;

  SevenZipExtractCallback(ISevenZipInArchive inArchive, SevenZipFile sevenZip, String directory, int type) {
    this.inArchive = inArchive;
    this.extractPath = directory;
    this.type = type;
    this.sevenZip = sevenZip;
  }

  @Override
  public ISequentialOutStream getStream(final int index, ExtractAskMode extractAskMode) throws SevenZipException {
    return new ISequentialOutStream() {

      @Override
      public int write(byte[] data) throws SevenZipException {
        String filePath = inArchive.getStringProperty(index, PropID.PATH);

        FileOutputStream fos = null;
        try {
          if (sevenZip.isValidType(filePath, type)) {
            fos = new FileOutputStream(extractPath + "/" + filePath, true);
            fos.write(data);
            sevenZip.extractedFiles.add(filePath);
          }
        } catch (IOException e) {
        } finally {
          try {
            if (fos != null) {
              fos.flush();
              fos.close();
            }
          } catch (IOException e) {
          }
        }
        return data.length;
      }
    };
  }

  @Override
  public void prepareOperation(ExtractAskMode extractAskMode) throws SevenZipException {
  }

  @Override
  public void setOperationResult(ExtractOperationResult extractOperationResult) throws SevenZipException {
  }

  @Override
  public void setCompleted(long completeValue) throws SevenZipException {
  }

  @Override
  public void setTotal(long total) throws SevenZipException {
  }
}
