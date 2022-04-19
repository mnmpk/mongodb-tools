
$(window).on('load', function() {
    removeServers = function(){
        newLines = "\n";
        newLines += "export REGIONS=("+$('#removeServersRegions').val()+")\n";
        newLines += "export OWNER=\""+$('#removeServersOwner').val()+"\"\n";
        newLines += "export TAGNAME=\""+$('#removeServersTagName').val()+"\"";
        $("#removeServers code").text(replaceFirstNLines($("#removeServers code").text(), newLines));
    }
    $('#removeServers .form-control').on('change', removeServers);

    launchInstances = function(){
        newLines = "\n";
        newLines += "export REGION=\""+$('#launchInstanceRegion').val()+"\"\n";
        newLines += "export SG_NAME=\""+$('#launchInstanceSGName').val()+"\"\n";
        newLines += "export SG_DESCRIPTION=\""+$('#launchInstanceSGDesc').val()+"\"\n";
        newLines += "export PG_NAME=\""+$('#launchInstancePGName').val()+"\"\n";
        newLines += "export ITYPE=\""+$('#launchInstanceInstanceType').val()+"\"\n";
        newLines += "export IMG_NAME=\""+$('#launchInstanceImageName').val()+"\"\n";
        newLines += "export DISKS='"+$('#launchInstanceDisks').val()+"'\n";
        newLines += "export KEY_NAME=\""+$('#launchInstanceKeyName').val()+"\"\n";
        newLines += "export TAGS='"+$('#launchInstanceTags').val()+"'\n";
        newLines += "export NO_OF_HOST="+$('#launchInstanceNoOfHosts').val();
        $("#launchInstance code").text(replaceFirstNLines($("#launchInstance code").text(), newLines));
    }
    $('#launchInstance .form-control').on('change', launchInstances);

});
function replaceFirstNLines(originalText, newLines){
    let lines = originalText.split('\n');
    lines.splice(0,newLines.split("\n").length);
    const newArray = [newLines].concat(lines)
    return newArray.join('\n');
}
