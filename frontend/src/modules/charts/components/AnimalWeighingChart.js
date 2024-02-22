import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {MoonLoader} from "react-spinners";
import ReactEcharts from "echarts-for-react";
import {getAllWeighingsByAnimalId} from "../../../backend/weighingService";


const AnimalWeighingChart = () => {

    const {animalId} = useParams();
    const [data, setData] = useState([]);
    const [showChart, setShowChart] = useState(true);
    const [isLoading, setIsLoading] = useState(true);
    const [backendErrors, setBackendErrors] = useState(null);

    let optionDefault = {
      title: {
          text: '',
          left: 'center',
          textStyle: {
              fontSize: 12
          }
      },
        toolbox: {
            feature: {
                dataView: { show: true, readOnly: true },
                magicType: { show: true, type: ['line', 'bar'] },
                restore: { show: true },
            }
        },
      tooltip: {
          trigger: 'axis',
          axisPointer: {
              type: 'shadow'
          },
          valueFormatter: (value) => value + ' kg'
      },
      legend: {},
      dataZoom: [
          {
              show: true,
          },
          {
              type: 'inside',
              show: true

          }
      ],
      xAxis: {
          type: 'category',
          data: []
      },
      yAxis: {
          type: 'value',
          axisLabel: {
              formatter: '{value} kg'
          }
      },
      series: [

      ]
    };
    const [option, setOption] = useState(optionDefault);

    useEffect(() => {
        const id = Number(animalId);
        if (isLoading) {
            // get chartData
            getAllWeighingsByAnimalId(id, (weighinDtos) => {
                console.log(weighinDtos);
                setData(weighinDtos);
                let data = {
                    data: [],
                    type: 'line',
                    smooth: true,
                    itemStyle: {
                        color: '#fc5858'
                    },
                    lineStyle: {
                        color: '#00B63E'
                    }
                };

                weighinDtos.forEach((dto) => {
                    data.data.push(dto.kilos);
                    optionDefault.xAxis.data.push(new Date(dto.date).toISOString().substring(0,10));
                });
                optionDefault.series.push(data);
                setOption(optionDefault);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            })
        }
    }, []);

    if (isLoading) {
        return (
            <div className="row justify-content-center">
                <div className="col-1 text-center">
                    <MoonLoader color="#97C99D" />
                </div>
            </div>
        );
    }

    return (
        <div className="card card-chart mt-3">
            <div className="row">
                <div className="col-12 ml-4 mt-2 chart-title" onClick={e => setShowChart(!showChart)}>
                    {showChart &&
                        <h4><b>Weight evolution <i className="fas fa-caret-up"></i></b></h4>
                    }
                    {!showChart &&
                        <h4><b>Weight evolution <i className="fas fa-caret-down"></i></b></h4>
                    }
                </div>
            </div>
            {showChart && data.length > 0 &&
                <ReactEcharts option={option} lazyUpdate={true}/>
            }
            {showChart && data.length === 0 &&
                <div className="row justify-content-center">
                    <div className="col-11">
                        <div className="alert alert-primary" role="alert">
                            This animal doesn't have weighings
                        </div>
                    </div>
                </div>
            }
        </div>
    );
}

export default AnimalWeighingChart;