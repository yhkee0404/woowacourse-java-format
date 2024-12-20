/*
 * Copyright 2024 The Woowacourse Java Format Authors. All Rights Reserved.
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.googlejavaformat.intellij;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

final class InitialConfigurationStartupActivity implements StartupActivity.Background {

  private static final String NOTIFICATION_TITLE = "Enable woowacourse-java-format";

  @Override
  public void runActivity(@NotNull Project project) {
    GoogleJavaFormatSettings settings = GoogleJavaFormatSettings.getInstance(project);

    if (settings.isUninitialized()) {
      settings.setEnabled(false);
      displayNewUserNotification(project, settings);
    } else if (settings.isEnabled()) {
      JreConfigurationChecker.checkJreConfiguration(project);
    }
  }

  private void displayNewUserNotification(Project project, GoogleJavaFormatSettings settings) {
    NotificationGroupManager groupManager = NotificationGroupManager.getInstance();
    NotificationGroup group = groupManager.getNotificationGroup(NOTIFICATION_TITLE);
    Notification notification =
        new Notification(
            group.getDisplayId(),
            NOTIFICATION_TITLE,
            "The woowacourse-java-format plugin is disabled by default. "
                + "<a href=\"enable\">Enable for this project</a>.",
            NotificationType.INFORMATION);
    notification.setListener(
        (n, e) -> {
          settings.setEnabled(true);
          n.expire();
        });
    notification.notify(project);
  }
}
