package routers

import (
	"github.com/gin-gonic/gin"

	"github.com/crazykulou/go-gin-example/pkg/setting"
	"github.com/crazykulou/go-gin-example/routers/api/v1"
)

func InitRouter() *gin.Engine{

	r := gin.New()

	r.Use(gin.Logger())

	r.Use(gin.Recovery())

	gin.SetMode(setting.RunMode)

	apiv1 := r.Group("/api/v1")
	{
		apiv1.GET("/articles",v1.GetArticles)
		apiv1.GET("/articles/:id",v1.GetArticle)
		apiv1.POST("/articles",v1.AddArticle)
		apiv1.PUT("/articles/:id",v1.EditArticle)
		apiv1.DELETE("/articles/:id",v1.DeleteArticle)
	}

	return r
}
