package es.udc.paproject.backend.rest.dtos;

public class MemberParamsDto {
	Long userId;
	Long groupId;

	public MemberParamsDto() {}

	public MemberParamsDto(Long userId, Long groupId) {
		this.userId = userId;
		this.groupId = groupId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
}
