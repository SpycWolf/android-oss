package com.kickstarter.viewmodels;


import android.support.annotation.NonNull;
import android.util.Pair;

import com.kickstarter.libs.ActivityViewModel;
import com.kickstarter.libs.Environment;
import com.kickstarter.libs.utils.NumberUtils;
import com.kickstarter.libs.utils.ProjectUtils;
import com.kickstarter.models.Project;
import com.kickstarter.models.ProjectStats;
import com.kickstarter.services.ApiClientType;
import com.kickstarter.ui.viewholders.CreatorDashboardHeaderViewHolder;
import com.kickstarter.viewmodels.inputs.CreatorDashboardHeaderHolderViewModelInputs;
import com.kickstarter.viewmodels.outputs.CreatorDashboardHeaderHolderViewModelOutputs;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public class CreatorDashboardHeaderHolderViewModel extends ActivityViewModel<CreatorDashboardHeaderViewHolder> implements
  CreatorDashboardHeaderHolderViewModelInputs, CreatorDashboardHeaderHolderViewModelOutputs {


  public CreatorDashboardHeaderHolderViewModel(final @NonNull Environment environment) {
    super(environment);

    projectAndStats
      .map(ps -> ps.first)
      .subscribe(this.currentProject);

    projectAndStats
      .map(ps -> ps.first)
      .map(Project::backersCount)
      .map(NumberUtils::format)
      .subscribe(projectBackersCountText);

    projectAndStats
      .map(ps -> ps.first)
      .map(ProjectUtils::deadlineCountdownValue)
      .map(NumberUtils::format)
      .subscribe(timeRemaining);

  }


  public final CreatorDashboardHeaderHolderViewModelInputs inputs = this;
  public final CreatorDashboardHeaderHolderViewModelOutputs outputs = this;

  private final PublishSubject<Pair<Project, ProjectStats>> projectAndStats = PublishSubject.create();
  private final BehaviorSubject<String> percentageFundedTextViewText = BehaviorSubject.create();
  private final BehaviorSubject<Project> currentProject = BehaviorSubject.create();
  private final BehaviorSubject<String> projectBackersCountText = BehaviorSubject.create();
  private final BehaviorSubject<String> timeRemaining = BehaviorSubject.create();

  @Override public void projectAndStats(Project project, ProjectStats projectStats) {
    this.projectAndStats.onNext(Pair.create(project, projectStats));
  }

  @Override public @NonNull Observable<String> percentageFundedTextViewText() { return this.percentageFundedTextViewText; }
  @Override public @NonNull Observable<Project> currentProject() { return this.currentProject; }
  @Override public @NonNull Observable<String> projectBackersCountText() {
    return this.projectBackersCountText;
  }
  @Override public @NonNull Observable<String> timeRemaining() { return this.timeRemaining; }
}
