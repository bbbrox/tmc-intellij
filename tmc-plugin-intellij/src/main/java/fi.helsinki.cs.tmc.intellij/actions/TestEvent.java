package fi.helsinki.cs.tmc.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.configuration.TmcSettings;
import fi.helsinki.cs.tmc.core.holders.TmcSettingsHolder;

public class TestEvent extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        DownloadExerciseAction action = new DownloadExerciseAction();
        try {
            action.downloadExerciseAction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

