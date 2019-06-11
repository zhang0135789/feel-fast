FROM java:8
EXPOSE 10080

VOLUME /tmp
ADD feel-fast.jar /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
