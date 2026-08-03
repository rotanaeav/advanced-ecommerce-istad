# Understand Instruction
FROM ghcr.io/graalvm/jdk-community:25i1
WORKDIR /workspace
COPY build/libs/advanced-ecommerce-1.0.jar /workspace/api.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar","/workspace/api.jar"]
