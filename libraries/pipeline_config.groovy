jte {
    allow_scm_jenkinsfile = 'True'
    permissive_initialization = 'True'
    //pipeline_template = 'my-named-template.groovy'
    reverse_library_resolution = 'False'
}
template_methods {
    prepare
    build
    test
    deploy
}