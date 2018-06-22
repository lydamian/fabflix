#include <stdio.h>
#include <string.h>
#include <stdlib.h>




int main(int argc, char *argv[]){

	char buf[100];
	long long jdbc_sum =0;
	long long servlet_time =0;
	int counter = 0;

	//storing sum into jdbc_sum and servlet_time
	while(fgets(buf, 100, stdin)){
		buf[strcspn(buf, "\n")] = 0;
		char *temp = strtok(buf,",");	
		temp=temp+0;
		long s_time = atoi(temp);
		servlet_time += s_time;

		temp = strtok(NULL, ",");
		long j_time = atoi(temp);
		jdbc_sum += j_time;
		++counter;
	}

	//calculating average by dividing by counter.
	jdbc_sum = jdbc_sum/counter;
	servlet_time = servlet_time/counter;
	printf("Servlet average is: %lld\n, Jdbc_Average is: %lld\n", servlet_time, jdbc_sum);

	return 0;
}

