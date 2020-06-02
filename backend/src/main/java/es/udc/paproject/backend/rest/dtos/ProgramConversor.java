package es.udc.paproject.backend.rest.dtos;

import static es.udc.paproject.backend.rest.dtos.UserConversor.toUserDtos;

import java.util.ArrayList;
import java.util.List;

import es.udc.paproject.backend.model.entities.Program;
import es.udc.paproject.backend.model.entities.User;

public class ProgramConversor {
	private ProgramConversor() {}

	public final static ProgramDto toProgramDto(Program program) {
		return new ProgramDto(program.getId(), program.getUser().getId(), program.getExercise() == null ? null : program.getExercise().getId(), 
			program.getUser().getUserName(), program.getProgramName(), program.getProgramDesc(),
			program.getCreationDate(), program.getUpdateDate(), program.getCode(), program.getPrivateProgram());
	}

	public final static List<ProgramDto> toProgramDtos(List<Program> programs) {
		List<ProgramDto> programDtos = new ArrayList<>();

		for (Program program : programs) {
			programDtos.add(new ProgramDto(program.getId(), program.getUser().getId(), program.getExercise() == null ? null : program.getExercise().getId(),
					program.getUser().getUserName(), program.getProgramName(), program.getProgramDesc(),
					program.getCreationDate(), program.getUpdateDate(), program.getCode(),
					program.getPrivateProgram()));
		}

		return programDtos;
	}

	public final static FullProgramDto toFullProgramDto(Program program, List<User> sharedUsers) {
		String visibility = "";
		if (program.getPrivateProgram()) {
			visibility = sharedUsers.size() == 0 ? "Private" : "Shared";
		} else {
			visibility = "Public";
		}

		return new FullProgramDto(program.getId(), program.getUser().getId(), program.getProgramName(),
				program.getProgramDesc(), program.getCreationDate(), program.getUpdateDate(),
				program.getCode(), program.getPrivateProgram(), visibility, toUserDtos(sharedUsers));
	}
}
