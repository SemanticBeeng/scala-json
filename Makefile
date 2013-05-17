compile:
	./sbt.sh compile
test:
	./sbt.sh test
update:
	./sbt.sh update
idea:
	./sbt.sh gen-idea
eclipse:
	./sbt.sh eclipse
clean:
	rm -rf .idea
	rm -rf .idea_modules
	rm -rf project
	rm -rf target
