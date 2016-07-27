package fi.helsinki.cs.tmc.intellij.actions;

import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.domain.ProgressObserver;
import fi.helsinki.cs.tmc.core.domain.submission.SubmissionResult;
import fi.helsinki.cs.tmc.intellij.holders.TmcCoreHolder;
import fi.helsinki.cs.tmc.intellij.services.CourseAndExerciseManager;
import fi.helsinki.cs.tmc.intellij.services.ObjectFinder;
import fi.helsinki.cs.tmc.intellij.ui.OperationInProgressNotification;
import fi.helsinki.cs.tmc.intellij.ui.submissionresult.SubmissionResultHandler;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * Uploads the currently active project to TMC Server
 * Defined in plugin.xml on the line
 * <action id="Upload Exercise" class="fi.helsinki.cs.tmc.intellij.actions.UploadExerciseAction"
 * Uses CourseAndExerciseManager to update the view after upload,
 * SubmissionResultHandler displays the returned results
 */

public class UploadExerciseAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        OperationInProgressNotification note = new
                OperationInProgressNotification("Uploading exercise, please wait!");
        Project project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        uploadExercise(project, TmcCoreHolder.get(), new ObjectFinder());
        note.hide();
    }

    private void uploadExercise(Project project, TmcCore core, ObjectFinder finder) {
        String path = project.getBasePath();
        String[] exerciseCourse;
        if (path.toString().contains("/")) {
            exerciseCourse = path.toString().split("/");
        } else {
            exerciseCourse = path.toString().split("\\\\");
        }
        if (CourseAndExerciseManager.isCourseInDatabase(exerciseCourse[exerciseCourse.length - 2])) {
            try {
                Course course =
                        finder.findCourseByName(exerciseCourse[exerciseCourse.length - 2], core);
                Exercise exercise = finder.findExerciseByName(course,
                        exerciseCourse[exerciseCourse.length - 1]);
                getResults(project, exercise, core);
                CourseAndExerciseManager.updateSinglecourse(course.getName());
            } catch (Exception exept) {
                Messages.showErrorDialog(project, "Are your credentials correct?\n"
                        + "Is this a TMC Exercise?\n"
                        + "Are you connected to the internet?\n"
                        + exept.getMessage() + " " + exept.toString(), "Error while submitting");
            }
        } else {
            Messages.showErrorDialog(project, "Project not identified as TMC exercise", "Error");
        }
    }

    private void getResults(Project project, Exercise exercise, TmcCore core) {
        try {
            SubmissionResult result = core.submit(ProgressObserver.NULL_OBSERVER, exercise).call();
            SubmissionResultHandler.showResultMessage(exercise, result, project);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
