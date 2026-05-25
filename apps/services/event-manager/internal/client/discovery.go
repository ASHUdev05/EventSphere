package client

import (
	"fmt"
	"github.com/hudl/fargo"
)

func GetServiceURL(eurekaConn fargo.EurekaConnection, appName string) (string, error) {
	app, err := eurekaConn.GetApp(appName)
	if err != nil || len(app.Instances) == 0 {
		return "", fmt.Errorf("service %s not found", appName)
	}
	return fmt.Sprintf("http://%s:%d", app.Instances[0].HostName, app.Instances[0].Port), nil
}