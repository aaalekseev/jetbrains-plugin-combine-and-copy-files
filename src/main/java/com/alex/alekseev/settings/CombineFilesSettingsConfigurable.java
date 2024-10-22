package com.alex.alekseev.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CombineFilesSettingsConfigurable implements Configurable {
    private JPanel mainPanel;
    private JTextField excludedExtensionsField;
    private JTextField excludedDirectoriesField;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Combine Files Settings";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel extensionsLabel = new JLabel("Excluded File Extensions (comma-separated):");
        excludedExtensionsField = new JTextField(30);

        JLabel directoriesLabel = new JLabel("Excluded Directories (comma-separated):");
        excludedDirectoriesField = new JTextField(30);

        mainPanel.add(extensionsLabel);
        mainPanel.add(excludedExtensionsField);
        mainPanel.add(Box.createVerticalStrut(10)); // Add some space between fields
        mainPanel.add(directoriesLabel);
        mainPanel.add(excludedDirectoriesField);

        return mainPanel;
    }

    @Override
    public boolean isModified() {
        CombineFilesSettingsState settingsState = CombineFilesSettingsState.getInstance();
        if (settingsState == null) {
            return false;
        }

        boolean modified = false;
        if (!excludedExtensionsField.getText().equals(settingsState.getExcludedExtensions())) {
            modified = true;
        }
        if (!excludedDirectoriesField.getText().equals(settingsState.getExcludedDirectories())) {
            modified = true;
        }
        return modified;
    }

    @Override
    public void apply() {
        CombineFilesSettingsState settingsState = CombineFilesSettingsState.getInstance();
        if (settingsState != null) {
            settingsState.setExcludedExtensions(excludedExtensionsField.getText());
            settingsState.setExcludedDirectories(excludedDirectoriesField.getText());
        }
    }

    @Override
    public void reset() {
        CombineFilesSettingsState settingsState = CombineFilesSettingsState.getInstance();
        if (settingsState != null) {
            excludedExtensionsField.setText(settingsState.getExcludedExtensions());
            excludedDirectoriesField.setText(settingsState.getExcludedDirectories());
        }
    }

    @Override
    public void disposeUIResources() {
        mainPanel = null;
        excludedExtensionsField = null;
        excludedDirectoriesField = null;
    }
}
