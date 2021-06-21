Keyed Windows

stream
       .keyBy(...)               <-  keyed versus non-keyed windows
       .window(...)              <-  required: "assigner"
      [.trigger(...)]            <-  optional: "trigger" (else default trigger)
      [.evictor(...)]            <-  optional: "evictor" (else no evictor)
      [.allowedLateness(...)]    <-  optional: "lateness" (else zero)
      [.sideOutputLateData(...)] <-  optional: "output tag" (else no side output for late data)
       .reduce/aggregate/fold/apply()      <-  required: "function"
      [.getSideOutput(...)]      <-  optional: "output tag"
Non-Keyed Windows

stream
       .windowAll(...)           <-  required: "assigner"
      [.trigger(...)]            <-  optional: "trigger" (else default trigger)
      [.evictor(...)]            <-  optional: "evictor" (else no evictor)
      [.allowedLateness(...)]    <-  optional: "lateness" (else zero)
      [.sideOutputLateData(...)] <-  optional: "output tag" (else no side output for late data)
       .reduce/aggregate/fold/apply()      <-  required: "function"
      [.getSideOutput(...)]      <-  optional: "output tag"

Window Lifecycle
Keyed vs Non-Keyed Windows
Window Assigners
    Tumbling Windows
    Sliding Windows
    Session Windows
    Global Windows
Window Functions
    ReduceFunction
    AggregateFunction
    FoldFunction
    ProcessWindowFunction
    ProcessWindowFunction with Incremental Aggregation
    Using per-window state in ProcessWindowFunction
    WindowFunction (Legacy)
Triggers
    Fire and Purge
    Default Triggers of WindowAssigners
    Built-in and Custom Triggers
Evictors
Allowed Lateness
    Getting late data as a side output
    Late elements considerations
Working with window results
    Interaction of watermarks and windows
    Consecutive windowed operations
Useful state size considerations
