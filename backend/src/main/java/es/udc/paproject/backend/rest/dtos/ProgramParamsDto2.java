package es.udc.paproject.backend.rest.dtos;

public class ProgramParamsDto2 {
	private Long programId;
	private String programName;
	private String programDesc;
	private String code;

	public ProgramParamsDto2() {}

	public ProgramParamsDto2(Long programId, String programName,
			String programDesc, String code) {
		this.programId = programId;
		this.programName = programName;
		this.programDesc = programDesc;
		this.code = code;
	}

	public final Long getProgramId() {
		return programId;
	}

	public final void setProgramId(Long id) {
		this.programId = id;
	}

	public final String getProgramName() {
		return programName;
	}

	public final void setProgramName(String programName) {
		this.programName = programName;
	}

	public final String getProgramDesc() {
		return programDesc;
	}

	public final void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}

	public final String getCode() {
		return code;
	}

	public final void setCode(String code) {
		this.code = code;
	}
}
