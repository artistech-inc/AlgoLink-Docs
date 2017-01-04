all: algolink libalgolink.a

clean:
	rm -rf *.o
	rm -rf algolink
	rm -rf lib*

algolink: libalgolink.a main.o
	g++ -o algolink main.o -L. -lalgolink -lcurl -ljson -lboost_program_options

libalgolink.a: algolink.o
	ar rcs libalgolink.a algolink.o

#libalgolink.so: algolink.o
#	rm -rf *.so*
#	g++ -shared -Wl,-soname,libalgolink.so.1 -o libalgolink.so.1.0.`svn info |grep Revision: |cut -c11-`  algolink.o
#	ln -s libalgolink.so.1.0.`svn info |grep Revision: |cut -c11-` libalgolink.so.1
#	ln -s libalgolink.so.1.0.`svn info |grep Revision: |cut -c11-` libalgolink.so

algolink.o: algolink.cpp algolink.h
	#g++ -fPIC -c algolink.cpp -DDEBUG
	g++ -fPIC -c algolink.cpp

main.o: main.cpp
	g++ -c main.cpp

