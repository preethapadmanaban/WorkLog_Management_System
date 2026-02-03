package com.worklog.commands.timesheets;

import java.util.ArrayList;
import java.util.List;

import com.worklog.entities.Task;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class NavigateCreateTimesheetPageCommand implements Command {

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {

		// System.out.println("inside navigation.");

		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("id") == null) {
			// System.out.println("inside navigat to timesheet creation.");
			throw new UnAuthorizedException("access_denied");
		}

		int id = (int) session.getAttribute("id");

		TaskDAO taskDAO = new TaskDAO();

		List<Task> tasks = taskDAO.getAllTasksForEmployee(id).orElse(new ArrayList<Task>());

		// here we convert this into json, because we use this list of task directly inside the script tag, not as jsp rendering.
		// Gson gson = new GsonBuilder()
		// .registerTypeAdapter(LocalDate.class,
		// (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
		// .registerTypeAdapter(LocalDate.class,
		// (JsonDeserializer<LocalDate>) (json, typeOfT, context) -> LocalDate.parse(json.getAsString()))
		// .create();
		// request.setAttribute("tasks", gson.toJson(tasks));
		request.setAttribute("tasks", tasks);

		// System.out.println("succesfully navigat to timesheet creation.");

		return true;
	}

}
