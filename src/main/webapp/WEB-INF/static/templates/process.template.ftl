<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <link href="styles/bootstrap.css" rel="stylesheet"/>
    <link href="styles/app.css" rel="stylesheet"/>
</head>
<body>
<div ng-show="processCtrl.queriesSize">
    <h3>Queries size: {{processCtrl.queriesSize}}</h3>
    <h3>Approximate search time: {{processCtrl.utilService.calculateSearchTime(processCtrl.queriesSize)}}</h3>
</div>

<div ng-hide="processCtrl.utilService.isLoading">
    <h1 class="file-title">Processing file: <br> {{processCtrl.fileName}}</h1>
</div>
<div>
    <ul class="nav nav-tabs">
        <li class="nav-item">
            <a class="nav-link active" href="#">Sections</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="#">Sources</a>
        </li>
    </ul>
</div>
<div class="scan-btn-container">
    <div ng-hide="processCtrl.utilService.isLoading">
        <h2>Found sections:</h2>
        <div class="found-sections" ng-hide="!processCtrl.sections">
            <div ng-repeat="section in processCtrl.sections">
                <button class="btn btn-primary" ng-click="processCtrl.sectionText = section.sectionText">Read</button>
                <button class="btn btn-primary" ng-show="section.subSections" ng-click="processCtrl.toggleSubSections($index, section.showSubSections)">Show
                    subsections
                </button>
                <label>
                    Section weight:
                    <input type="number"
                           min="-5"
                           max="5"
                           value="0">
                </label>
                <h3 class="badge badge-primary section-name">{{section.sectionName}}</h3>
                <div ng-show="section.showSubSections" ng-repeat="subsection in section.subSections">
                    <button class="btn btn-primary" ng-click="processCtrl.sectionText = subsection.sectionText">Read</button>
                    <label>
                        Subsection weight:
                        <input type="number"
                               min="-5"
                               max="5"
                               value="0">
                    </label>
                    <h3 class="badge badge-light section-name">{{subsection.sectionName}}</h3>
                </div>
            </div>
        </div>
    </div>

    <div ng-hide="processCtrl.utilService.isLoading">
        <div class="card file-text" ng-hide="!processCtrl.text">
            <h3 class="card-header text-white card-primary no-margin file-card">File Text:</h3>
            <div class="card-block">
                <p class="card-text file-text-content" ng-bind="processCtrl.text"></p>
            </div>
        </div>
    </div>

    <div ng-hide="!processCtrl.sectionText">
        <div class="card file-text" ng-hide="!processCtrl.text">
            <h3 class="card-header text-white section-card">Section Text:</h3>
            <div class="card-block">
                <p class="card-text file-text-content" ng-bind="processCtrl.sectionText"></p>
            </div>
        </div>
    </div>

    <div class="scan-btn-holder" ng-hide="processCtrl.utilService.isLoading">
        <button class="btn btn-primary scan-btn">
            Scan!
        </button>
    </div>

</div>

<div ng-show="processCtrl.utilService.isLoading">
    Loading...
</div>
</body>
</html>