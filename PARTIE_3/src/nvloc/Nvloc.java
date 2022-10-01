package nvloc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Nvloc {
    private final File file;
    public Nvloc(File f) {
        this.file = f;
    }

    public int getNonEmptyLinesCount() {
        int nonEmptyLines = 0;
        if (this.file.exists() && this.file.canRead() && this.file.isFile()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.matches("\\s*")) nonEmptyLines++;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return nonEmptyLines;
    }
}
