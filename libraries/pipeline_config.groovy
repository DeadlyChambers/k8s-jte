jte{
  allow_scm_jenkinsfile = False
  permissive_initialization = True
  //pipeline_template = "my-named-template.groovy"
  reverse_library_resolution = True
}
template_methods{
  prepare
  build
  test
  deploy
}