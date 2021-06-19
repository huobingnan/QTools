package openq.model

data class BondLengthResult(
    val analyseKeyFrameList: List<AnalyseKeyFrame>,
    val data: List<Map<String, Double>>
) {

}