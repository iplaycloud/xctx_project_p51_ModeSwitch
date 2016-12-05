
cmake_minimum_required(VERSION 3.4.1)

add_library(native-game-event-service-lib  SHARED  src/main/cpp/native-game-event-service.cpp)
add_library(native-uart-lib  SHARED  src/main/cpp/native-uart-control.cpp)

#libcutils libutils liblog libbinder
find_library(log-lib    log)

target_link_libraries(native-game-event-service-lib   ${log-lib})
target_link_libraries(native-uart-lib   ${log-lib})

