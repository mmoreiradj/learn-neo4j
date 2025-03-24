deploy:
  k3d cluster ls | grep neo4j || k3d cluster create neo4j --registry-create neo4j-registry:0.0.0.0:5000 -p "8443:443@loadbalancer" -p "8080:80@loadbalancer"
  helm repo add neo4j https://equinor.github.io/helm-charts/charts/
  helm repo update
  helm upgrade --install neo4j neo4j/neo4j-community -f values.dev.yaml

pf:
  kubectl port-forward svc/neo4j 7474:7474 7687:7687

delete:
  k3d cluster delete neo4j
