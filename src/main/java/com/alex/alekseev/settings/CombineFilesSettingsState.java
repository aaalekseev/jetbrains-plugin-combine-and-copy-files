package com.alex.alekseev.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@State(
        name = "com.alex.alekseev.settings.CombineFilesSettingsState",
        storages = {@Storage("CombineFilesSettings.xml")}
)
@Service(Service.Level.APP)
public final class CombineFilesSettingsState implements PersistentStateComponent<CombineFilesSettingsState> {

    private String excludedExtensions = "pyc,class,log,tmp,so,dylib"; // Default exclusions
    private String excludedDirectories = ""; // Default to none

    public static CombineFilesSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(CombineFilesSettingsState.class);
    }

    @Nullable
    @Override
    public CombineFilesSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CombineFilesSettingsState state) {
        this.excludedExtensions = state.excludedExtensions;
        this.excludedDirectories = state.excludedDirectories;
    }

    public String getExcludedExtensions() {
        return excludedExtensions;
    }

    public void setExcludedExtensions(String excludedExtensions) {
        this.excludedExtensions = excludedExtensions;
    }

    public List<String> getExcludedExtensionsAsList() {
        return Arrays.stream(excludedExtensions.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    public String getExcludedDirectories() {
        return excludedDirectories;
    }

    public void setExcludedDirectories(String excludedDirectories) {
        this.excludedDirectories = excludedDirectories;
    }

    public List<String> getExcludedDirectoriesAsList() {
        return Arrays.stream(excludedDirectories.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
