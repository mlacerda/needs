# Needs

Versão básica para stack full client js (angular/require) c/ Spring MVC.

## Roadmap

![Image of Yaktocat](https://docs.google.com/drawings/d/1y_tJIqpOHXN4SxYFf3vYWggj_td6lhPqaJmu6b1J2Yk/pub?w=495&h=379)
  

## Arquitetura
TODO: ???

## Desenvolvimento


### Prerequisites para ambiente de desenvolvimento
As seguintes ferramentas precisam estar instalada para o correto funcionando do ambiente de desenvolvimento
```
Eclipse
Java 7: apt-get install openjdk7
Maven 3: apt-get install maven
git: apt-get install git
npm: apt-get install npm 
bower: npm install -g bower 
```

## Executando a aplicação
```
    git clone https://github.com/marcuslacerda/needs
    bower install
    mvn package && java -jar target/megarural-0.2.0.jar
```

### Parametros
* -Dfile.encoding="UTF-8"
* src/main/resources/config/application.yml contem as configurações de startup da aplicação

### Estrutura de arquivos

    needs/
    
     |____ src/ 
     |      |____ [main]/ 
     |      |      |____ java/ (server side impl)
     |      |      |____ resource/        
     |      |      |____ webapp/ (client side impl)
                         |____ app/
                               |____ i18n/
                               |____ system/
                               |_____modules/
                               
            |____ resources/ (config files)
     |      |
     |      |____ test/ (code examples)
     |____
     |____ bower.json
     |____ readme.md
     |____ pom.xml