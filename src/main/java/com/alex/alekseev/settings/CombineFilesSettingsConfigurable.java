package com.alex.alekseev.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CombineFilesSettingsConfigurable implements Configurable {
    private JPanel mainPanel;
    private JTextField excludedExtensionsField;

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

        JLabel label = new JLabel("Excluded File Extensions (comma-separated):");
        excludedExtensionsField = new JTextField(30);

        mainPanel.add(label);
        mainPanel.add(excludedExtensionsField);

        return mainPanel;
    }

    @Override
    public boolean isModified() {
        CombineFilesSettingsState settingsState = CombineFilesSettingsState.getInstance();
        if (settingsState == null) {
            return false;
        }

        String currentExclusions = settingsState.getExcludedExtensions();
        return !excludedExtensionsField.getText().equals(currentExclusions);
    }

    @Override
    public void apply() {
        CombineFilesSettingsState settingsState = CombineFilesSettingsState.getInstance();
        if (settingsState != null) {
            String userExclusions = excludedExtensionsField.getText();
            settingsState.setExcludedExtensions(userExclusions);
        }
    }

    @Override
    public void reset() {
        CombineFilesSettingsState settingsState = CombineFilesSettingsState.getInstance();
        if (settingsState != null) {
            String savedExclusions = settingsState.getExcludedExtensions();
            excludedExtensionsField.setText(savedExclusions);
        }
    }

    @Override
    public void disposeUIResources() {
        mainPanel = null;
        excludedExtensionsField = null;
    }
}
