
import Foundation
import Dispatch
import Java
import OpenCombine

// Hashing(encryption) the password and validating against the confirm password by decryption.
// Executes callback in main activity and sends the validation result
public func updateTasksList(activity: JObject)
{

    activity.call(
      // return the error result string
      method: "printTasks", "tasksstringhere")

}

// NOTE: Use @_silgen_name attribute to set native name for a function called from Java
@_silgen_name("Java_com_example_swiftandroidexample_MainActivity_initTaskManager")
public func MainActivity_initTaskManager(
  env: UnsafeMutablePointer<JNIEnv>, activity: JavaObject
) {
  // Create JObject wrapper for activity object
  let mainActivity = JObject(activity)
      let viewModel = TaskListViewModel()

      // Example usage
      let taskCancellable = viewModel.tasksPublisher
        .sink { tasks in
          if tasks.isEmpty {
            print("No tasks available.")
          } else {
            print("Current tasks:\(tasks.count)")
          }
        }
      // put while
      let task1 = Task(id: 1, title: "Task-one")
      let task2 = Task(id: 2, title: "Task-two")

      viewModel.addTask(task1)
      viewModel.addTask(task2)
      viewModel.removeTask(task1)

updateTasksList(activity: mainActivity)

}

  // Model for a task
  struct Task: Identifiable {
    let id: Int
    let title: String
  }

  // ViewModel to manage tasks
  class TaskListViewModel {
    var tasks: [Task] = []
    let tasksPublisher = PassthroughSubject<[Task], Never>()

    func addTask(_ task: Task) {
      tasks.append(task)
      print("Task added: \(task.title)")
      tasksPublisher.send(tasks)
    }

    func removeTask(_ task: Task) {
      tasks.remove(at: task.id - 1)
      print("Task removed: \(task.title)")
      tasksPublisher.send(tasks)
    }

    func printTasks() {
      if tasks.isEmpty {
        print("No tasks available.")
      } else {
        print("Current tasks:")
        for task in tasks {
          print("\(task.id). \(task.title)")
        }
      }
    }
  }


