package es.udc.paproject.backend.rest.dtos;

public class GroupParamsDto {

	Long userId;
	String groupName;

	public GroupParamsDto() {}

	public GroupParamsDto(Long userId, String groupName) {
		this.userId = userId;
		this.groupName = groupName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
