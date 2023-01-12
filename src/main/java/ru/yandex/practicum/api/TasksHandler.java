package ru.yandex.practicum.api;

import com.google.gson.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import ru.yandex.practicum.managers.HttpTaskManager;
import ru.yandex.practicum.managers.ManagerNotFoundException;

import ru.yandex.practicum.tasks.*;
import ru.yandex.practicum.utils.*;

import java.io.IOException;

import java.net.URI;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static ru.yandex.practicum.api.APIMessage.*;
import static ru.yandex.practicum.api.Endpoint.*;
import static ru.yandex.practicum.api.RequestMethod.GET;

public class TasksHandler implements HttpHandler {
	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	private static final String PATH_PARTS_SEPARATOR = "/";
	private static final int START_INDEX_ID = 3;
	private final Gson gson;
	private final HttpTaskManager manager;
	public JsonElement body;
	private HttpExchange exchange;
	private URI url;
	private RequestMethod method;

	public TasksHandler(HttpTaskManager httpManager) {
		gson = GsonConfig.getGsonTaskConfig();
		this.manager = httpManager;
	}

	private int getParameterID(URI url) {
		return Integer.parseInt(url.getQuery().substring(START_INDEX_ID));
	}

	private void sendResponse(String responseString, int responseCode) throws IOException {
		System.out.println("/sendResponce");
		if (responseString.isBlank()) {
			exchange.sendResponseHeaders(responseCode, 0);
			return;
		}
		byte[] response = responseString.getBytes(DEFAULT_CHARSET);
		exchange.getResponseHeaders().add(CONTENT_TYPE.name(), APPLICATION_JSON.name());
		exchange.sendResponseHeaders(responseCode, response.length);
		exchange.getResponseBody().write(response);
		exchange.close();
	}

	private void handleGetAllTasks() throws IOException {
		System.out.println("/getAllTasks");
		sendResponse(gson.toJson(manager.getAllTasks()), 200);
	}

	private void handleGetAllSubtasks() throws IOException {
		System.out.println("/getAllSubtasks");
		sendResponse(gson.toJson(manager.getAllSubTasks()), 200);
	}

	private void handleGetAllEpics() throws IOException {
		System.out.println("/getAllEpics");
		sendResponse(gson.toJson(manager.getAllEpics()), 200);
	}

	private void handleGetTaskByID() throws IOException {
		System.out.println("/getTaskByID");
		int id = getParameterID(url);
		try {
			sendResponse(gson.toJson(manager.getTaskByID(id)), 200);
		} catch (ManagerNotFoundException e) {
			sendResponse(gson.toJson(e.getMessage()), 404);
		}
	}

	private void handleGetSubtaskByID() throws IOException {
		System.out.println("/getSubtaskByID");
		int id = getParameterID(url);
		try {
			sendResponse(gson.toJson(manager.getSubTaskByID(id)), 200);
		} catch (ManagerNotFoundException e) {
			sendResponse(gson.toJson(e.getMessage()), 404);
		}
	}

	private void handleGetEpicByID() throws IOException {
		System.out.println("/getEpicByID");
		int id = getParameterID(url);
		try {
			sendResponse(gson.toJson(manager.getEpicByID(id)), 200);
		} catch (ManagerNotFoundException e) {
			sendResponse(gson.toJson(e.getMessage()), 404);
		}
	}

	private void handleGetEpicSubtasks() throws IOException {
		System.out.println("/getEpicSubtasks");
		int id = getParameterID(url);
		try {
			sendResponse(gson.toJson(manager.getAllSubTasksByEpic(id)), 200);
		} catch (ManagerNotFoundException e) {
			sendResponse(gson.toJson(e.getMessage()), 404);
		}
	}

	private void handleGetPrioritizedTasks() throws IOException {
		System.out.println("/getPrioritizedTasks");
		sendResponse(gson.toJson(manager.getPrioritizedTasks()), 200);
	}

	private void handleGetHistory() throws IOException {
		System.out.println("/getHistory");
		sendResponse(gson.toJson(manager.getHistory()), 200);
	}

	private void handleDeleteAllTasks() throws IOException {
		System.out.println("/deleteAllTasks");
		manager.deleteAllTasks();
		sendResponse(gson.toJson(APIMessage.DELETE_ACCEPT.getMessage()), 200);
	}

	private void handleDeleteAllSubtasks() throws IOException {
		System.out.println("/deleteAllSubtasks");
		manager.deleteAllSubTasks();
		sendResponse(gson.toJson(APIMessage.DELETE_ACCEPT.getMessage()), 200);
	}

	private void handleDeleteAllEpics() throws IOException {
		System.out.println("/deleteAllEpics");
		manager.deleteAllEpics();
		sendResponse(gson.toJson(APIMessage.DELETE_ACCEPT.getMessage()), 200);
	}

	private void handleDeleteTaskByID() throws IOException {
		System.out.println("/deleteTaskByID");
		int id = getParameterID(url);
		try {
			manager.deleteTaskByID(id);
			sendResponse(gson.toJson(APIMessage.DELETE_ACCEPT.getMessage()), 200);
		} catch (ManagerNotFoundException e) {
			sendResponse(gson.toJson(APIMessage.NOT_FOUND.getMessage()), 404);
		}
	}

	private void handleDeleteSubtaskByID() throws IOException {
		System.out.println("/deleteSubtaskByID");
		int id = getParameterID(url);
		try {
			manager.deleteSubTaskByID(id);
			sendResponse(gson.toJson(APIMessage.DELETE_ACCEPT.getMessage()), 200);
		} catch (ManagerNotFoundException e) {
			sendResponse(gson.toJson(APIMessage.NOT_FOUND.getMessage()), 404);
		}
	}

	private void handleDeleteEpicByID() throws IOException {
		System.out.println("/deleteEpicByID");
		int id = getParameterID(url);
		try {
			manager.deleteEpicByID(id);
			sendResponse(gson.toJson(APIMessage.DELETE_ACCEPT.getMessage()), 200);
		} catch (ManagerNotFoundException e) {
			sendResponse(gson.toJson(APIMessage.NOT_FOUND.getMessage()), 404);
		}
	}

	private void handleCreateTask() throws IOException {
		System.out.println("/createTask");
		try {
			Task task = gson.fromJson(body, Task.class);
			manager.create(task);
			sendResponse(gson.toJson(task), 200);
		} catch (TimeValueException | JsonParseException e) {
			sendResponse(e.getMessage(), 400);
		}
	}

	private void handleUpdateTask() throws IOException {
		System.out.println("/updateTask");
		try {
			Task task = gson.fromJson(body, Task.class);
			manager.update(task);
			sendResponse(gson.toJson(task), 200);
		} catch (ManagerNotFoundException | TimeValueException | JsonParseException e) {
			sendResponse(e.getMessage(), 400);
		}

	}

	private void handleCreateSubtask() throws IOException {
		System.out.println("/createSubtask");
		try {
			SubTask subTask = gson.fromJson(body, SubTask.class);
			try {
				manager.getEpicByID(subTask.getParentEpicID());
			} catch (ManagerNotFoundException e) {
				sendResponse(gson.toJson(PARENT_EPIC_NOT_FOUND.getMessage()), 400);
				throw new APIException(PARENT_EPIC_NOT_FOUND.getMessage());
			}
			manager.create(subTask);
			sendResponse(gson.toJson(subTask), 200);
		} catch (TimeValueException | JsonParseException e) {
			sendResponse(e.getMessage(), 400);
		}
	}

	private void handleUpdateSubtask() throws IOException {
		System.out.println("/updateSubtask");
		try {
			SubTask subTask = gson.fromJson(body, SubTask.class);
			manager.update(subTask);
			sendResponse(gson.toJson(subTask), 200);
		} catch (ManagerNotFoundException | TimeValueException | JsonParseException e) {
			sendResponse(e.getMessage(), 400);
		}

	}

	private void handleCreateEpic() throws IOException {
		System.out.println("/createEpic");
		try {
			Epic epic = gson.fromJson(body, Epic.class);
			manager.create(epic);
			sendResponse(gson.toJson(epic), 200);
		} catch (TimeValueException | JsonParseException e) {
			sendResponse(e.getMessage(), 400);
		}
	}

	private void handleUpdateEpic() throws IOException {
		System.out.println("/updateEpic");
		try {
			Epic epic = gson.fromJson(body, Epic.class);
			manager.update(epic);
			sendResponse(gson.toJson(epic), 200);
		} catch (ManagerNotFoundException | TimeValueException | JsonParseException e) {
			sendResponse(e.getMessage(), 400);
		}

	}

	public boolean isHaveIdInBody() { //todo
		boolean result;
		result = body != null
				&& body.isJsonObject()
				&& body.getAsJsonObject().has("id")
				&& !body.getAsJsonObject().get("id").isJsonNull();
		return result;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		System.out.println("/handle");
		this.exchange = exchange;
		url = exchange.getRequestURI();
		method = HttpConverter.getEnumMethod(exchange.getRequestMethod());
		body = JsonParser.parseString(new String(exchange.getRequestBody().readAllBytes()));
		Endpoint endpoint = getEndpoint(url, method);

		switch (endpoint) {
			case GET_ALL_TASKS:
				handleGetAllTasks();
				break;
			case GET_ALL_SUBTASKS:
				handleGetAllSubtasks();
				break;
			case GET_ALL_EPICS:
				handleGetAllEpics();
				break;
			case GET_TASK_BY_ID:
				handleGetTaskByID();
				break;
			case GET_SUBTASK_BY_ID:
				handleGetSubtaskByID();
				break;
			case GET_EPIC_BY_ID:
				handleGetEpicByID();
				break;
			case GET_ALL_SUBTASKS_BY_EPIC:
				handleGetEpicSubtasks();
				break;
			case GET_PRIORITIZED_TASKS:
				handleGetPrioritizedTasks();
				break;
			case GET_HISTORY:
				handleGetHistory();
				break;
			case DELETE_ALL_TASKS:
				handleDeleteAllTasks();
				break;
			case DELETE_ALL_SUBTASKS:
				handleDeleteAllSubtasks();
				break;
			case DELETE_ALL_EPICS:
				handleDeleteAllEpics();
				break;
			case DELETE_TASK_BY_ID:
				handleDeleteTaskByID();
				break;
			case DELETE_SUBTASK_BY_ID:
				handleDeleteSubtaskByID();
				break;
			case DELETE_EPIC_BY_ID:
				handleDeleteEpicByID();
				break;
			case CREATE_TASK:
				handleCreateTask();
				break;
			case UPDATE_TASK:
				handleUpdateTask();
				break;
			case CREATE_SUBTASK:
				handleCreateSubtask();
				break;
			case UPDATE_SUBTASK:
				handleUpdateSubtask();
				break;
			case CREATE_EPIC:
				handleCreateEpic();
				break;
			case UPDATE_EPIC:
				handleUpdateEpic();
				break;
		}
	}

    public Endpoint getEndpoint(URI url, RequestMethod method) {
        String[] pathParts = url.getPath().split(PATH_PARTS_SEPARATOR);
        boolean isHaveID = url.getQuery() != null;

        if (pathParts.length == 2 && method == GET) {
            return Endpoint.GET_PRIORITIZED_TASKS;
        }
        PathPart pathPart = HttpConverter.getEnumPathPart(pathParts[2]);
        if (pathParts.length == 3) {
            switch (pathPart) {
                case TASK:
                    switch (method) {
                        case GET:
                            return isHaveID ? GET_TASK_BY_ID : GET_ALL_TASKS;
                        case DELETE:
                            return isHaveID ? DELETE_TASK_BY_ID : DELETE_ALL_TASKS;
                        case POST:
                            return isHaveIdInBody() ? UPDATE_TASK : CREATE_TASK;
                    }
                case SUBTASK:
                    switch (method) {
                        case GET:
                            return isHaveID ? GET_SUBTASK_BY_ID : GET_ALL_SUBTASKS;
                        case DELETE:
                            return isHaveID ? DELETE_SUBTASK_BY_ID : DELETE_ALL_SUBTASKS;
                        case POST:
                            return isHaveIdInBody() ? UPDATE_SUBTASK : CREATE_SUBTASK;
                    }
                case EPIC:
                    switch (method) {
                        case GET:
                            return isHaveID ? GET_EPIC_BY_ID : GET_ALL_EPICS;
                        case DELETE:
                            return isHaveID ? DELETE_EPIC_BY_ID : DELETE_ALL_EPICS;
                        case POST:
                            return isHaveIdInBody() ? UPDATE_EPIC : CREATE_EPIC;
                    }
                case HISTORY:
                    return GET_HISTORY;
            }
        }
        if (pathParts.length == 4 && isHaveID) {
            return GET_ALL_SUBTASKS_BY_EPIC;
        }
        return UNKNOWN;
    }
}