module github.com/crazykulou/go-gin-example

go 1.15

replace (
	github.com/crazykulou/go-gin-example/conf => ./conf
	github.com/crazykulou/go-gin-example/models => ./models
	github.com/crazykulou/go-gin-example/pkg/setting => ./pkg/setting
	github.com/crazykulou/go-gin-example/routers => ./routers
)

require (
	github.com/gin-gonic/gin v1.6.3
	github.com/go-ini/ini v1.62.0
	github.com/unknwon/com v1.0.1
	gopkg.in/ini.v1 v1.62.0 // indirect
	gorm.io/driver/mysql v1.0.3
	gorm.io/gorm v1.20.8
)
