FROM ubuntu:18.04

RUN useradd -ms /bin/bash pkarcz
RUN adduser pkarcz sudo

ENV HOME=/home/pkarcz

WORKDIR ${HOME}

RUN apt-get update && apt-get install -y curl vim git unzip zip tar wget

RUN curl -s "https://get.sdkman.io" | bash
RUN chmod a+x "$HOME/.sdkman/bin/sdkman-init.sh"
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install java 11.0.11.j9-adpt"
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install kotlin"
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install gradle 7.2"
