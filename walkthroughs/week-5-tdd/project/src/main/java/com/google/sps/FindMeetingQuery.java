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

/* Implementation requires that Collection of Events are all sorted in order of
   meeting start time */
public final class FindMeetingQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<TimeRange> availableTimes = new ArrayList<>();

    /* If the requested time span is longer than a whole day, return no available meeting times */
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return availableTimes;
    }

    int blockStart = TimeRange.START_OF_DAY;
    int blockStartOpt = TimeRange.START_OF_DAY;
    Collection<String> requestAtten = request.getAttendees();
    Collection<String> optionalAtten = request.getOptionalAttendees();

    /* If there are only optional attendees requested, default set all optional attendees to be 
       included */
    boolean optionalIncluded = requestAtten.isEmpty();
    
    for (Event event : events) { 
      /* Check if there are attendees in the event that are also requested attendees */
      Collection<String> eventAtten = event.getAttendees();
      boolean timeConflictFound = !Collections.disjoint(eventAtten, requestAtten);
      boolean timeConflictFoundOpt = !Collections.disjoint(eventAtten, optionalAtten);
      int eventStart = event.getWhen().start();
      int eventEnd = event.getWhen().end();

      /* If no optional attendee conflict and event length long enough, include optional attendee */
      if (!timeConflictFoundOpt && eventStart - blockStartOpt >= request.getDuration()) {
        optionalIncluded = true;
      }
      else if (timeConflictFoundOpt) {
        blockStartOpt = eventEnd;
      }

      if (optionalIncluded) {
        timeConflictFound =  timeConflictFound || timeConflictFoundOpt;
      }

      if (timeConflictFound) {
        /* If time conflict found, add available time block up until the start of the event */
        addIfDurationFits(availableTimes, request, blockStart, eventStart);
        /* If available start time is before the end of the conflict time block, 
           set new available time to after the conflict event is finished. */
        if (blockStart < eventEnd) {
          blockStart = eventEnd;
        }
      }
    }
    /* After all events considered, add the entire rest of the day as also available */
    addTillEndOfDay(availableTimes, blockStart);
    /* If there are no available times returned, but there are non 
       optional requested attendees, check again for non optional 
       attendee avaliability */
    if (availableTimes.isEmpty() && !requestAtten.isEmpty()) {
      queryRequiredOnly(events, request);
    }
    return availableTimes;
  }

  private void queryRequiredOnly(Collection<Event> events, MeetingRequest request) {
    Collection<TimeRange> availableTimes = new ArrayList<>();
    /* If the requested time span is longer than a whole day, return no available meeting times */
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return;
    }
    int blockStart = TimeRange.START_OF_DAY;
    Collection<String> requestAtten = request.getAttendees();
    for (Event event : events) { 
    /* Check if there are attendees in the event that are also requested attendees */
      Collection<String> eventAtten = event.getAttendees();
      int eventStart = event.getWhen().start();
      int eventEnd = event.getWhen().end();
      boolean timeConflictFound = !Collections.disjoint(eventAtten, requestAtten);
      /* If time conflict found, add available time block up until the start of the event */
      if (timeConflictFound) {
        addIfDurationFits(availableTimes, request, blockStart, eventStart);
        /* If available start time is before the end of the conflict time block, 
           set new available time to after the conflict event is finished. */
        if (blockStart < eventEnd) {
          blockStart = eventEnd;
        }
      }
    }
    addTillEndOfDay(availableTimes, blockStart);
  }

  /* After all events have been considered, add the entire time range from the last event time till 
     the end of day if the last event doesn't extend past the end of day */
  private void addTillEndOfDay(Collection<TimeRange> availableTimes, int blockStart) {
    if (blockStart < TimeRange.END_OF_DAY) {
      availableTimes.add(TimeRange.fromStartEnd(blockStart, TimeRange.END_OF_DAY, true));
    }
  }

  /* If block start time and block end time for the proposed meeting time range satisfies the 
     requested meeting block duration, then add time range to collection of available times */
  private void addIfDurationFits(Collection<TimeRange> availableTimes,  MeetingRequest request, int blockStart, int blockEnd) {
    if (blockEnd - blockStart >= request.getDuration()) {
      availableTimes.add(TimeRange.fromStartEnd(blockStart, blockEnd, false));
    }
  }
}
