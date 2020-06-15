// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<TimeRange> avaliableTimes = new ArrayList<>();

    /* If the requested time span is longer than a whole day, return no avaliable meeting times */
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return avaliableTimes;
    }

    int blockStart = TimeRange.START_OF_DAY;
    Collection<String> requestAtten = request.getAttendees();

    for (Event event : events) { 

      /* Check if there are attendees in the event that are also requested attendees */
      Collection<String> eventAtten = event.getAttendees();
      boolean timeConflictFound = !Collections.disjoint(eventAtten, requestAtten);

      if (timeConflictFound) {

        /* If time conflict found, add avaliable time block up until the start of the event */
        int blockEnd = event.getWhen().start();
        if (blockEnd - blockStart >= request.getDuration()) {
          avaliableTimes.add(TimeRange.fromStartEnd(blockStart, blockEnd, false));
        }

        /* If avaliable start time is before the end of the conflict time block, 
           set new avaliable time to after the conflict event is finished. */
        if (blockStart < event.getWhen().end()) {
          blockStart = event.getWhen().end();
        }
      }
    }

    /* After all events considered, add the entire rest of the day as also avaliable */
    if (blockStart < TimeRange.END_OF_DAY) {
      avaliableTimes.add(TimeRange.fromStartEnd(blockStart, TimeRange.END_OF_DAY, true));
    }
    return avaliableTimes;
  }
}
