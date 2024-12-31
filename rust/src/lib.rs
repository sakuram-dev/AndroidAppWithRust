#[no_mangle]
pub extern "C" fn Java_com_example_androidappwithrust_RustLib_isPrime(env: *mut jni::JNIEnv, class: jni::objects::JClass, n: i32) -> bool {
//pub extern "C" fn isPrime(n: i32) -> bool {

    if n <= 1 {
        return false;
    }
    for i in 2..=((n as f64).sqrt() as i32) {
        if n % i == 0 {
            return false;
        }
    }
    true
}