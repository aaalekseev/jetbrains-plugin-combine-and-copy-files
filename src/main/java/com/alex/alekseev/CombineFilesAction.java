package com.alex.alekseev;

import com.alex.alekseev.settings.CombineFilesSettingsState;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CombineFilesAction extends AnAction implements ClipboardOwner {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // Get selected files
        VirtualFile[] files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);

        if (files == null || files.length == 0) {
            Messages.showInfoMessage("No files selected", "Information");
            return;
        }

        // Get user-defined exclusions from settings
        List<String> excludedExtensions = CombineFilesSettingsState.getInstance().getExcludedExtensionsAsList();

        // Find the common base path
        String commonBasePath = findCommonBasePath(files);
        if (commonBasePath == null) {
            Messages.showErrorDialog("Could not determine a common directory for the selected files", "Error");
            return;
        }

        // Combine contents, skipping excluded file types
        StringBuilder combinedContent = new StringBuilder();
        for (VirtualFile file : files) {
            if (shouldExcludeFile(file, excludedExtensions)) {
                // Skip excluded files
                continue;
            }

            String relativePath = getRelativePath(commonBasePath, file.getPath());
            combinedContent.append("=== ").append(relativePath).append(" ===\n");
            try {
                combinedContent.append(new String(file.contentsToByteArray())).append("\n");
            } catch (IOException ex) {
                ex.printStackTrace();
                Messages.showErrorDialog("Failed to read file: " + file.getName(), "Error");
                return;
            }
        }

        // Copy combined content to clipboard
        try {
            StringSelection stringSelection = new StringSelection(combinedContent.toString());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, this);
            Messages.showInfoMessage("Combined content copied to clipboard.", "Success");
        } catch (Exception ex) {
            ex.printStackTrace();
            Messages.showErrorDialog("Failed to copy to clipboard: " + ex.getMessage(), "Error");
        }
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        // No action needed when clipboard ownership is lost
    }

    // Method to determine if a file should be excluded based on its extension and user settings
    private boolean shouldExcludeFile(VirtualFile file, List<String> excludedExtensions) {
        String extension = file.getExtension();
        return extension != null && excludedExtensions.contains(extension.toLowerCase());
    }

    private String findCommonBasePath(VirtualFile[] files) {
        Path commonPath = Paths.get(files[0].getPath()).getParent();
        for (VirtualFile file : files) {
            Path filePath = Paths.get(file.getPath()).getParent();
            commonPath = commonPath(filePath, commonPath);
            if (commonPath == null) {
                break;
            }
        }
        return commonPath != null ? commonPath.toString() : null;
    }

    private Path commonPath(Path path1, Path path2) {
        int count = Math.min(path1.getNameCount(), path2.getNameCount());
        Path result = path1.getRoot();
        for (int i = 0; i < count; i++) {
            if (path1.getName(i).equals(path2.getName(i))) {
                result = result.resolve(path1.getName(i));
            } else {
                break;
            }
        }
        return result.getNameCount() > 0 ? result : null;
    }

    private String getRelativePath(String basePath, String fullPath) {
        Path base = Paths.get(basePath);
        Path full = Paths.get(fullPath);
        return base.relativize(full).toString();
    }
}
