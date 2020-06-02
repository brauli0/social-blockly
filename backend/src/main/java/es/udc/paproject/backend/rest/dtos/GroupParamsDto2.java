package es.udc.paproject.backend.rest.dtos;

public class GroupParamsDto2 {

	Long groupId;
	String groupName;
	Boolean chatEnable;

	public GroupParamsDto2() {}

	public GroupParamsDto2(Long groupId, String groupName, Boolean chatEnable) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.chatEnable = chatEnable;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Boolean getChatEnable() {
		return chatEnable;
	}

	public void setChatEnable(Boolean chatEnable) {
		this.chatEnable = chatEnable;
	}
}
