compile:
	./sbt.sh compile -feature -deprecation
test:
	./sbt.sh test -feature -deprecation
update:
	./sbt.sh update
idea:
	./sbt.sh gen-idea
eclipse:
	./sbt.sh eclipse
clean:
	rm -rf .idea
	rm -rf .idea_modules
	rm -rf project/target
	rm -rf target
