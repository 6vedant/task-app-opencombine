import Dispatch
import Foundation
import Java
import OpenCombine

public func updateTasksList(activity: JObject) {

  // Example usage
  let viewModel = TaskListViewModel.viewModel

  let val = viewModel.getTasksResultString()
  activity.call(
    // return the error result string
    method: "printTasks", "\(val)")
}

// NOTE: Use @_silgen_name attribute to set native name for a function called from Java
@_silgen_name("Java_com_example_swiftandroidexample_MainActivity_initTaskManager")
public func MainActivity_initTaskManager(
  env: UnsafeMutablePointer<JNIEnv>, activity: JavaObject
) {
  // Create JObject wrapper for activity object
  let mainActivity = JObject(activity)
  let viewModel = TaskListViewModel.viewModel

  updateTasksList(activity: mainActivity)
}

// NOTE: Use @_silgen_name attribute to set native name for a function called from Java
@_silgen_name("Java_com_example_swiftandroidexample_MainActivity_addTask")
public func MainActivity_addTask(
  env: UnsafeMutablePointer<JNIEnv>, activity: JavaObject,
  taskName: JavaString
) {
  // Create JObject wrapper for activity object
  let mainActivity = JObject(activity)

  // Convert the Java string to a Swift string
  let taskTitle = String.fromJavaObject(taskName)

  let viewModel = TaskListViewModel.viewModel

  let task1 = Task(id: viewModel.tasks.count, title: taskTitle)

  viewModel.addTask(task1)
  updateTasksList(activity: mainActivity)
}

// NOTE: Use @_silgen_name attribute to set native name for a function called from Java
@_silgen_name("Java_com_example_swiftandroidexample_MainActivity_removeTask")
public func MainActivity_removeTask(
  env: UnsafeMutablePointer<JNIEnv>, activity: JavaObject,
  taskName: JavaString
) {
  // Create JObject wrapper for activity object
  let mainActivity = JObject(activity)

  // Convert the Java string to a Swift string
  let taskTitle = String.fromJavaObject(taskName)

  let viewModel = TaskListViewModel.viewModel

  for task in viewModel.tasks {
    if task.title == taskTitle {
      viewModel.removeTask(task)
    }
  }

  updateTasksList(activity: mainActivity)
}

// Model for a task
struct Task: Identifiable {
  let id: Int
  let title: String
}

// ViewModel to manage tasks
class TaskListViewModel {
  static let viewModel = TaskListViewModel()
  private init() {
  }
  var tasks: [Task] = []
  let tasksPublisher = PassthroughSubject<[Task], Never>()

  func addTask(_ task: Task) {
    tasks.append(task)
    print("Task added: \(task.title)")
    tasksPublisher.send(tasks)
  }

  func removeTask(_ task: Task) {
    tasks.remove(at: task.id)
    print("Task removed: \(task.title)")
    tasksPublisher.send(tasks)
  }

  func getTasksResultString() -> String {
    var resultString = ""
    if tasks.isEmpty {

    } else {
      print("Current tasks:")
      for task in tasks {
        resultString += "\(task.title)#@"
      }
    }
    return resultString
  }
}
