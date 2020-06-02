package es.udc.paproject.backend.rest.dtos;

import java.util.Calendar;

public class MessageParamsDto2 {
	Long groupId;
	Calendar begin;
	Calendar end;

	public MessageParamsDto2() {}

	public MessageParamsDto2(Long groupId, Calendar begin, Calendar end) {
		this.groupId = groupId;
		this.begin = begin;
		this.end = end;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Calendar getBegin() {
		return begin;
	}

	public void setBegin(Calendar begin) {
		this.begin = begin;
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}
}
