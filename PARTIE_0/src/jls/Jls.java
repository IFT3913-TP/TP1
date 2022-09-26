package jls;

import java.io.File;

public class Jls {
    private final File baseDirectory;
    private final String[][] fileListTab;
    private int fileListSize;

    public Jls(String path) {
        this.baseDirectory = new File(path);
        this.fileListTab = new String[256][3];
        this.fileListSize = 0;
    }

    private void computeFileList(File path) {
        if (this.fileListSize <= 255) {
            File directory = new File(path.getAbsolutePath());
            File[] contents = directory.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    if (f.isFile()) {
                        String fileName = f.getName();
                        if (fileName.contains(".")) {
                            int extensionIndex = fileName.lastIndexOf(".") + 1;
                            if (fileName.substring(extensionIndex).equals("java")) {
                                String fRPath = this.baseDirectory.toURI().relativize(f.toURI()).getPath();
                                this.fileListTab[fileListSize][0] = "./" + fRPath;
                                String packageName = fRPath.replace(fileName, "");
                                if (packageName.contains("/")) {
                                    if (packageName.lastIndexOf("/") == packageName.length() - 1) {
                                        packageName = packageName.substring(0, packageName.length() - 1);
                                    }
                                }
                                packageName = packageName.replace("/", ".");
                                this.fileListTab[fileListSize][1] = packageName;
                                this.fileListTab[fileListSize][2] = fileName.substring(0, extensionIndex - 1);
                                this.fileListSize++;
                            }
                        }
                    }
                    File[] innerContents = f.listFiles();
                    if (innerContents != null) {
                        String fPath = f.getAbsolutePath();
                        computeFileList(new File(fPath));
                    }
                }
            }
        }
    }

    /*    public String[][] getFileListTab() {
        computeFileList(this.baseDirectory);
        return fileListTab;
    }*/

    public String getCSVOutput() {
        StringBuilder result = new StringBuilder();
        computeFileList(this.baseDirectory);
        for (int i = 0; i < this.fileListSize; i++) {
            result.append(fileListTab[i][0]).append(", ");
            result.append(fileListTab[i][1]).append(", ");
            result.append(fileListTab[i][2]).append("\n");
        }
        return result.toString();
    }
}
