package es.udc.paproject.backend.rest.dtos;

public class GroupIdDto {
	private Long groupId;

	public GroupIdDto() {}

	public GroupIdDto(Long groupId) {
		this.groupId = groupId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
}
