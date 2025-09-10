workspace {

    model {
        user = person "Usuário" "Interage com a API REST para enviar e receber dados."

        system = softwareSystem "Sistema de Processamento de Dados" "Processa e armazena dados do usuário." {
            api = container "API REST" "Exposição dos serviços do sistema" "Spring Boot"
            db = container "Banco de Dados" "Armazenamento persistente dos dados" "PostgreSQL"
            queue = container "Fila de Mensagens" "Comunicação assíncrona entre serviços" "RabbitMQ"
        }

        user -> api "Interage via HTTP"
        api -> db "Consulta e grava dados"
        api -> queue "Publica mensagens"
        queue -> api "Consome mensagens"
    }

    views {
        systemContext system {
            include *
            autolayout lr
        }
        container system {
            include *
            autolayout lr
        }
        component api {
            include *
            autolayout lr
        }
    }

    //theme default
}
