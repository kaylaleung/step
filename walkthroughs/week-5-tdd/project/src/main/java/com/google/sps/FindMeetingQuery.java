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

  /** 
   * Requires that collection of events are sorted in order of meeting start time. 
   */
public final class FindMeetingQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<TimeRange> availableTimes = new ArrayList<>();

    // If the requested time span is longer than a whole day, return no available meeting times
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return availableTimes;
    }

    availableTimes = queryTimes(events, request, request.getAttendees());
    
    if (!request.getOptionalAttendees().isEmpty()) {
      Collection<TimeRange> availableOptTimes = queryTimes(events, request, request.getOptionalAttendees());

      // If there are no mandatory attendees, then return times that work for all optional attendees
      if (request.getAttendees().isEmpty()) {
        return availableOptTimes;
      }
      else {
        // Get the meeting times that work for for both mandatory and optional attendees if any
        return getOverlap(availableTimes, availableOptTimes, request);
      }      
    }

    return availableTimes;
  }

  public Collection<TimeRange> queryTimes(Collection<Event> events, MeetingRequest request, Collection<String> attendees) {
    Collection<TimeRange> availableTimes = new ArrayList<>();
    int blockStart = TimeRange.START_OF_DAY;
    
    for (Event event : events) { 
      // Check if there are attendees in the event that are also requested attendees
      Collection<String> eventAtten = event.getAttendees();
      boolean timeConflictFound = !Collections.disjoint(eventAtten, attendees);
      if (timeConflictFound) {
        // If time conflict found, add available time block up until the start of the event 
        addIfDurationFits(availableTimes, request, blockStart, event.getWhen().start());

        if (blockStart < event.getWhen().end()) {
          //  If available start time is before the end of the conflict time block, 
          //  set new available time to after the conflict event is finished. 
          blockStart = event.getWhen().end();
        }
      }
    }
    // After all events considered, add the entire rest of the day as also available
    addTillEndOfDay(availableTimes, blockStart);
    return availableTimes;
  }

  public Collection<TimeRange> getOverlap(Collection<TimeRange> attenTimes, Collection<TimeRange> optTimes, MeetingRequest request ) {
    Collection<TimeRange> availableTimes = new ArrayList<>();

    // If there are no avaliable times for optional attendees, just return mandatory attendee times
    if (optTimes.isEmpty()) {
      return attenTimes;
    }

    for (TimeRange attenRange : attenTimes) {
      for (TimeRange optRange : optTimes) {
        int blockStart = attenRange.start();
        int blockEnd = attenRange.start() + attenRange.duration();
        
        if (attenRange.start() < optRange.start()) {
          blockStart = optRange.start();
        }
        if (attenRange.start() + attenRange.duration() > optRange.start() + optRange.duration()) {
          blockEnd = optRange.start() + optRange.duration();
        }

        // Verify that overlap time range satisfies meeting duration requirements
        if (blockEnd - blockStart >= request.getDuration()) {
          availableTimes.add(TimeRange.fromStartEnd(blockStart, blockEnd, false));
        }
      }
    }
    if (availableTimes.isEmpty()) {
      return attenTimes;
    }
    return availableTimes;
  }

  /**
   * After all events have been considered, add the entire time range from the last event time till 
   * the end of day if the last event doesn't extend past the end of day 
   */
  private void addTillEndOfDay(Collection<TimeRange> availableTimes, int blockStart) {
    if (blockStart < TimeRange.END_OF_DAY) {
      availableTimes.add(TimeRange.fromStartEnd(blockStart, TimeRange.END_OF_DAY, true));
    }
  }

  /** 
   * If block start time and block end time for the proposed meeting time range satisfies the
   * requested meeting block duration, then add time range to collection of available times. 
   */
  private void addIfDurationFits(Collection<TimeRange> availableTimes,  MeetingRequest request, int blockStart, int blockEnd) {
    if (blockEnd - blockStart >= request.getDuration()) {
      availableTimes.add(TimeRange.fromStartEnd(blockStart, blockEnd, false));
    }
  }
}
