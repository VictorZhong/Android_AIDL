// IAidl.aidl
package com.victor;

// Declare any non-default types here with import statements

interface IAidl {
    // say hello to name
    String sayHello(String name);
    // 获取进程ID
    int getPid();
}
