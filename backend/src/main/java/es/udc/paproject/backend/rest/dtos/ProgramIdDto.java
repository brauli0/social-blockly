package es.udc.paproject.backend.rest.dtos;

public class ProgramIdDto {
	private Long programId;

	public ProgramIdDto() {}

	public ProgramIdDto(Long programId) {
		this.programId = programId;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}
}
