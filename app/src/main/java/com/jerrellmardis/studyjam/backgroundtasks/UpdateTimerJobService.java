package com.jerrellmardis.studyjam.backgroundtasks;

import android.content.Intent;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;

public class UpdateTimerJobService extends JobService {

  /**
   * The entry point to your Job. Implementations should offload work to another thread of
   * execution as soon as possible.
   *
   * This is called by the Job Dispatcher to tell us we should start our job. Keep in mind this
   * method is run on the application's main thread, so we need to offload work to a background
   * thread.
   *
   * @return whether there is more work remaining.
   */
  @Override public boolean onStartJob(final JobParameters jobParameters) {
    // start the update timer service
    startService(new Intent(UpdateTimerJobService.this, UpdateTimerService.class));
    return true;
  }

  /**
   * Called when the scheduling engine has decided to interrupt the execution of a running job,
   * most likely because the runtime constraints associated with the job are no longer satisfied.
   *
   * @return whether the job should be retried
   * @see Job.Builder#setRetryStrategy(RetryStrategy)
   * @see RetryStrategy
   */
  @Override public boolean onStopJob(JobParameters jobParameters) {
    return true;
  }
}
