# Needs

Versão básica para stack full client js (angular/require) c/ Spring MVC.

## Arquitetura
  TODO: colocar uma imagem com visao geral das tecnologias aqui...

## Desenvolvimento


### Prerequisites para ambiente de desenvolvimento
As seguintes ferramentas precisam estar instalada para o correto funcionando do ambiente de desenvolvimento
```
1. Java 7: apt-get install openjdk7
2. Maven 3: apt-get install maven
3. git: apt-get install git
4. npm: apt-get install npm 
5. bower: npm install -g bower 
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