import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

	public int solution(String S) {
		String text = prepareDaysForSorting(S);
		List<String> meetings = splitDaysIntoStringList(text);
		List<Meeting> meetingTimes = getMeetingTimes(meetings);
		long longestFreeTime = getLongestFreeTime(meetingTimes);
		return (int) longestFreeTime;
	}

	private List<String> splitDaysIntoStringList(String text) {
		List<String> meetings = new ArrayList<>();
		int index = 0;
		while (index < text.length()) {
			meetings.add(text.substring(index, Math.min(index + 14, text.length())));
			index += 14;
		}
		return meetings;
	}

	/**
	 * Finds the longest free time between two meetings
	 */
	private long getLongestFreeTime(List<Meeting> meetingTimes) {
		long longestFreeTime = -1;

		meetingTimes.add(0, new Meeting(LocalDateTime.of(2011,1 , 1, 0, 0), false));
		meetingTimes.add(meetingTimes.size(), new Meeting(LocalDateTime.of(2011,1 , 8, 0, 0), true)); // next Monday (counted as work)

		LocalDateTime freeTimeStart = LocalDateTime.of(2011,1 , 1, 0, 0);
		for (Meeting meetingTime : meetingTimes) {
			long lastFreeStartTime = 0;
			if (meetingTime.isStart) {
				lastFreeStartTime = Duration.between(freeTimeStart, meetingTime.time).getSeconds() / 60;
			} else {
				freeTimeStart = meetingTime.time;
			}
			if (longestFreeTime < lastFreeStartTime) longestFreeTime = lastFreeStartTime;
		}

		return longestFreeTime;
	}

	/**
	 * Parsing string list elements into Meeting
	 */
	private List<Meeting> getMeetingTimes(List<String> meetings) {
		List<Meeting> meetingTimes = new ArrayList<>();
		Collections.sort(meetings);
		for (String meeting : meetings) {
			int day = Integer.parseInt(String.valueOf(meeting.charAt(0)));
			int meetingStartHour = Integer.valueOf(meeting.substring(2,4));
			int meetingStartMinute = Integer.valueOf(meeting.substring(5,7));
			meetingTimes.add(new Meeting(LocalDateTime.of(2011,1 , day, meetingStartHour, meetingStartMinute), true));

			int meetingEndHour = Integer.valueOf(meeting.substring(8,10));
			int meetingEndMinute = Integer.valueOf(meeting.substring(11,13));
			if (meetingEndHour == 24) { // because LocalDateTime hour 24 not accepted
				meetingEndHour = 0;
				day++;
			}
			meetingTimes.add(new Meeting(LocalDateTime.of(2011,1 , day, meetingEndHour, meetingEndMinute), false));
		}
		return meetingTimes;
	}


	private static class Meeting {
		LocalDateTime time;
		boolean isStart;

		public Meeting(LocalDateTime time, boolean isStart) {
			this.time = time;
			this.isStart = isStart;
		}

		@Override
		public String toString() {
			return "Meeting{" +
					"time=" + time +
					", isStart=" + isStart +
					'}';
		}
	}

	/**
	 * Replace day-s for numbers, to help ordering
	 */
	private static String prepareDaysForSorting(String s) {
		String result = s.replace("Mon", "1");
		result = result.replace("Tue", "2");
		result = result.replace("Wed", "3");
		result = result.replace("Thu", "4");
		result = result.replace("Fri", "5");
		result = result.replace("Sat", "6");
		result = result.replace("Sun", "7");
		return result;
	}

}
