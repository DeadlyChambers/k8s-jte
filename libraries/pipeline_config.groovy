jte {
    allow_scm_jenkinsfile = true
    permissive_initialization = true
    //pipeline_template = 'my-named-template.groovy'
    reverse_library_resolution = true
}
template_methods {
    prepare
    build
    test
    deploy
}
libraries {
    dotnet {

    }
}