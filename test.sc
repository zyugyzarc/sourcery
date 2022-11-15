03 {$START} 00

<- $$01
++ 01 01

== 02 {$END}
-- 02 $01

0? 02 -4
++ 02 03
== 00 $02

{LABEL START} {TEXT Hello World} 0a {LABEL END}