
import Foundation
import Dispatch
import Java
import Crypto


// Hashing(encryption) the password and validating against the confirm password by decryption.
// Executes callback in main activity and sends the validation result
public func validatePassword(activity: JObject, passwordStr: String, confirmPasswordStr: String)
{
  do {
    // Generate a random salt
    let salt = generateRandomSalt()

    // Hash the password
    let hashedPassword = try hashPassword(passwordStr, salt: salt)

    // Verify a password
    let isPasswordValid = try verifyPassword(confirmPasswordStr, against: hashedPassword, salt: salt)

    // call the Java method from Swift
    // Using the activity instance, call the `onPasswordValidated()` with result string
    if isPasswordValid {
      activity.call(method: "onPasswordValidated", "Password is valid!")
    } else {
      activity.call(method: "onPasswordValidated", "Password is not valid!")
    }
  } catch {
    var userInfoStr = ""
    if let nsError = error as? NSError {
      userInfoStr = "\(nsError.userInfo)"
    }
    activity.call(
      // return the error result string
      method: "onPasswordValidated", "ERROR at the function")
  }
}

// Function to hash a password securely using Swift Crypto
public func hashPassword(_ password: String, salt: Data) throws -> String {
  let passwordData = Data(password.utf8)

  // Concatenate the salt and password
  let saltedPassword = salt + passwordData

  // Hash the salted password using SHA-256
  let hashedPassword = SHA256.hash(data: saltedPassword)

  // Encode the hashed password as base64 for storage
  return Data(hashedPassword).base64EncodedString()
}

// Function to verify a password against a hashed value
public func verifyPassword(_ password: String, against hashedPassword: String, salt: Data) throws
  -> Bool
{
  // Decode the stored hashed password from base64
  guard let storedHashedPasswordData = Data(base64Encoded: hashedPassword) else {
    return false
  }

  let passwordData = Data(password.utf8)

  // Concatenate the salt and provided password
  let saltedPassword = salt + passwordData

  // Hash the salted password using SHA-256
  let computedHashedPassword = SHA256.hash(data: saltedPassword)

  // Verify if the computed hash matches the stored hash
  return computedHashedPassword == storedHashedPasswordData
}

// Function to generate a random salt
public func generateRandomSalt() -> Data {
  var randomBytes = [UInt8](repeating: 0, count: 16)
  var generator = SystemRandomNumberGenerator()
  randomBytes.withUnsafeMutableBufferPointer { buffer in
    for i in buffer.indices {
      buffer[i] = generator.next()
    }
  }
  return Data(randomBytes)
}

// NOTE: Use @_silgen_name attribute to set native name for a function called from Java
@_silgen_name("Java_com_example_swiftandroidexample_MainActivity_validatePassword")
public func MainActivity_validatePassword(
  env: UnsafeMutablePointer<JNIEnv>, activity: JavaObject, passwordString: JavaString,
  confirmPasswordString: JavaString
) {
  // Create JObject wrapper for activity object
  let mainActivity = JObject(activity)

  // Convert the Java string to a Swift string
  let passwordStr = String.fromJavaObject(passwordString)
  let confirmPasswordStr = String.fromJavaObject(confirmPasswordString)

  // Start the password validation
  validatePassword(activity: mainActivity, passwordStr: passwordStr, confirmPasswordStr: confirmPasswordStr)

}
